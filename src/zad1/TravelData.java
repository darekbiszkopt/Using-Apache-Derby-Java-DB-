package zad1;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TravelData {
    private ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
    private ArrayList<String> resList = null;
    private File file;
    private Scanner sc;
    private Locale localeSaved;

    public TravelData(File dataDir) {
        try {
            file = new File(dataDir + "\\dane");
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.println("File read exe");
        }

        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String[] tokens = line.split("\\t");
            ArrayList<String> tmpList = new ArrayList<String>(Arrays.asList(tokens));

            dataList.add(tmpList);
        }
    }

    public List<String> getOffersDescriptionsList(String localeString, String dateFormat) {
        resList = new ArrayList<String>();
        String[] tokens = localeString.split("_");
        String resString = "";

        if(tokens.length > 1) {
            localeSaved = new Locale(tokens[0], tokens[1]);
        }else {
            localeSaved = new Locale(tokens[0]);
        }

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) DateFormat.getDateInstance();
        simpleDateFormat.applyPattern(dateFormat);

        Date data = null;
        for (ArrayList<String> list : dataList) {
            String[] tokensTmp = list.get(0).split("_");
            Locale locale = null;

            if(tokensTmp.length > 1) {
                locale = new Locale(tokensTmp[0], tokensTmp[1]);
            }else {
                locale = new Locale(tokensTmp[0]);
            }

            Locale.setDefault(locale);

            Locale[] locales = Locale.getAvailableLocales();
            Locale localeTranslate = null;

            for (int i = 0; i < locales.length; i++) {
                if(locales[i].getDisplayCountry().equals(list.get(1)))
                    localeTranslate = locales[i];
            }
            resString += localeTranslate.getDisplayCountry(localeSaved);

            try {
                data = simpleDateFormat.parse(list.get(2));
                resString += "	" + simpleDateFormat.format(data);

                data = simpleDateFormat.parse(list.get(3));
                resString += "	" + simpleDateFormat.format(data);
                ResourceBundle labels = ResourceBundle.getBundle("zad1.LabelsBundle", localeSaved);
                resString += "	" + labels.getString(list.get(4));

                NumberFormat nFormat = NumberFormat.getInstance(locale);
                Number number = nFormat.parse(list.get(5));
                resString += "	" + nFormat.format(number);

                Currency currency = Currency.getInstance(list.get(6));
                resString += "	" + currency.getCurrencyCode();

                resList.add(resString);
                resString = "";

            } catch (ParseException e) {
                System.err.println("Format exe");
                System.exit(2);
            }
        }
        return resList;
    }

    public ArrayList<String> getResList() {
        return resList;
    }
}
