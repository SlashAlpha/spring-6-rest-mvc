package slash.code.spring6restmvc.services;

import slash.code.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCSVService {

    List<BeerCSVRecord> convertCsv(File csvFile);

}
