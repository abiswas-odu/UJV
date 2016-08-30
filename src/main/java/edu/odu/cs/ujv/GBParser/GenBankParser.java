package edu.odu.cs.ujv.GBParser;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.GenbankReader;
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderParser;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.biojava.nbio.core.sequence.location.template.Location;


/**
 * This class is responsible for parsing the input GenBank files
 */
public class GenBankParser {
    public static final Logger logger = LogManager.getLogger(GenBankParser.class);
    public static void main(String[] args) {
        try (FileInputStream is = new FileInputStream(
                new File(GenBankParser.class.getResource("/NC_010612.gbk").toURI()))) {

            GenbankReader<DNASequence, NucleotideCompound> dnaReader =
                    new GenbankReader<>(is, new GenericGenbankHeaderParser<>(),
                            new DNASequenceCreator(
                                    DNACompoundSet.getDNACompoundSet()));
            /*
            Map<String, DNASequence> dnaSequences = dnaReader.process();
            DNASequence theSequence = dnaSequences.get("NC_010612");

            List<String> featureKeyWords = theSequence.getFeaturesKeyWord().getKeyWords();
           featureKeyWords.forEach(System.out::println);
            */
           List<GenbankRecord<NucleotideCompound>> records =  dnaReader.process().entrySet().stream().map(/*(String,DNASequence)*/sequenceEntry -> {
                DNASequence theSequence = sequenceEntry.getValue();
                //the distinct names of the features present in the DNASequence
                final Set<String> featureNameSet = new HashSet<>();

                List<FeatureSet<NucleotideCompound>> featureSetList = theSequence.getFeatures()
                        .stream()
                                //transform the featureInterface into FeatureSets
                                //adding the features name to the featureNameSet
                        .map(featureInterface -> {
                            featureNameSet.add(featureInterface.getDescription());
                            //logger.debug(featureInterface.getLocations());
                            return new FeatureSet<>(theSequence, featureInterface);
                        })
                                //collect each transformation and return to use new List containing each feature
                        .collect(Collectors.toList());
                return new GenbankRecord<>(sequenceEntry.getKey(), theSequence, featureSetList,featureNameSet);
            }).collect(Collectors.toList());
            GenbankRecord<NucleotideCompound> gr = records.get(0);
            gr.forEach(fs -> logger.debug("Start: "+fs.getStart()+" End: "+fs.getEnd()));

        } catch (IOException| CompoundNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static List<GenbankRecord<NucleotideCompound>> getFullFeatureSet(File genbankFile) throws IOException,CompoundNotFoundException {
        //The GenbankReader closes the inputstream for us
        FileInputStream is = new FileInputStream(genbankFile);
        GenbankReader<DNASequence, NucleotideCompound> dnaReader =
                new GenbankReader<>(is, new GenericGenbankHeaderParser<>(),
                        new DNASequenceCreator(
                                DNACompoundSet.getDNACompoundSet()));
        logger.debug("starting parsing");
        //for each Map<String,DNASequence> returned to us by process get stream over its entrySet
        return dnaReader.process().entrySet().stream().map(/*(String,DNASequence)*/sequenceEntry -> {
            DNASequence theSequence = sequenceEntry.getValue();
            //the distinct names of the features present in the DNASequence
            final Set<String> featureNameSet = new HashSet<>();

            List<FeatureSet<NucleotideCompound>> featureSetList = theSequence.getFeatures()
                    .stream()
                    //transform the featureInterface into FeatureSets
                    //adding the features name to the featureNameSet
                    .map(featureInterface -> {
                        featureNameSet.add(featureInterface.getDescription());
                        //logger.debug(featureInterface.getLocations());
                        return new FeatureSet<>(theSequence, featureInterface);
                    })
                    //collect each transformation and return to use new List containing each feature
                    .collect(Collectors.toList());
            return new GenbankRecord<>(sequenceEntry.getKey(), theSequence, featureSetList,featureNameSet);
        }).collect(Collectors.toList());
    }

    public static Task<List<GenbankRecord<NucleotideCompound>>> getFileLoadingTask(File genbankFile){
        return new FileLoadingTask(genbankFile);
    }

    private static class FileLoadingTask extends Task<List<GenbankRecord<NucleotideCompound>>> {
        private File fileToLoad;
        public FileLoadingTask(File fileToLoad){
            this.fileToLoad = fileToLoad;
        }

        @Override
        protected List<GenbankRecord<NucleotideCompound>> call() throws Exception {
            return GenBankParser.getFullFeatureSet(fileToLoad);
        }
    }
}
