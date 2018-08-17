package Utils;

import es.accenture.flink.Utils.Exceptions.KuduClientException;
import org.apache.kudu.client.KuduException;


public class ReadKuduTable {

    public static void main(String[] args) {

        String table = ""; // TODO insert table name
        String host = "localhost";

        try {
            Utils utils = new Utils(host);
            utils.readTablePrint(table);
        } catch (KuduClientException e) {
            e.printStackTrace();
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }
}