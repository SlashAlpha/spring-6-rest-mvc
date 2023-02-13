package slash.code.spring6restmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import slash.code.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class BeerCSVServiceImpl implements BeerCSVService {
    @Override
    public List<BeerCSVRecord> convertCsv(File csvFile) {
        try {
            List<BeerCSVRecord> beerCSVRecords=new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                    .withType(BeerCSVRecord.class)
                    .build().parse();
    return beerCSVRecords;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
   return null; }
}
