package edu.odu.cs.ujv.GBParser;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;

import java.io.File;
import java.util.List;


public class FileLoadingService extends Service<List<GenbankRecord<NucleotideCompound>>> {
    private File fileToLoad;
    public void setFileToLoad(File fileToLoad){
        this.fileToLoad = fileToLoad;
    }
    @Override
    protected Task<List<GenbankRecord<NucleotideCompound>>> createTask() {
        return GenBankParser.getFileLoadingTask(fileToLoad);
    }
}
