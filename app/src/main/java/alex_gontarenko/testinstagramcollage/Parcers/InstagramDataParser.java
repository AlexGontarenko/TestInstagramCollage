package alex_gontarenko.testinstagramcollage.Parcers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Alex on 21.06.2014.
 */
public abstract class InstagramDataParser<T> {

    public abstract T parceData(InputStream is);

    protected String streamToString(InputStream is) throws IOException {
        String string = "";

        if (is != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            string = stringBuilder.toString();
        }

        return string;
    }
}
