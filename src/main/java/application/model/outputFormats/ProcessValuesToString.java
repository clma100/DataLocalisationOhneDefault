package application.model.outputFormats;

import lombok.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Regulates the Presentation of relativ and absolut values properly. Additionally all values are rounded to eight
 * decimal places and then properly returned as String.
 */
@Data
public class ProcessValuesToString {

    public String getValueString(Double value) {
        String valueString;
        DecimalFormat df = new DecimalFormat("#.########");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);

        if (value < 1 && value > 0) {
            valueString = df.format(value);
        } else {
            valueString = String.valueOf((int) (value + 0.5));
        }
        return valueString;
    }
}
