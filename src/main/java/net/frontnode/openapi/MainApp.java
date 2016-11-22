package net.frontnode.openapi;

import net.frontnode.openapi.model.FundSearchInfo;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiankuan
 *         9/11/15.
 */
public class MainApp {

    public static void main(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        Options options = new Options();
        options.addOption("host", true, "yingmi openapi host name");
        options.addOption("port", true, "port number (default 443)");
        options.addOption("sslv3", false, "force SSLv3 (default TLSv1.2/1.1)");
        options.addOption("key", true, "api key");
        options.addOption("secret", true, "api secret");
        options.addOption("keytype",true,"keystore type, use -Djavax.net.ssl.keyStoreType if absent, falls back to 'jks'");
        options.addOption("keystore", true, "keystor path, use -Djavax.net.ssl.keyStore if absent");
        options.addOption("keypass", true, "keystore password, use -Djavax.net.ssl.keyStorePassword if absent");
        options.addOption("h", "help", false, "show usage");

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            showUsage(options);
            System.exit(-1);
        }

        if (commandLine.hasOption("h")) {
            showUsage(options);
            System.exit(0);
        }

        Map<String, String> params = new HashMap<>();
        String[] paramNames = new String[]{"host", "key", "secret"};
        for (String paramName: paramNames) {
            if (commandLine.hasOption(paramName)) {
                params.put(paramName, commandLine.getOptionValue(paramName));
            } else {
                System.err.println("缺少必要参数" + paramName);
                showUsage(options);
                System.exit(-1);
            }
        }

        // optional params
        params.put("keytype", commandLine.getOptionValue("keytype",System.getProperty("javax.net.ssl.keyStoreType","jks")));
        params.put("keystore", commandLine.getOptionValue("keystore",System.getProperty("javax.net.ssl.keyStore")));
        params.put("keypass", commandLine.getOptionValue("keypass",System.getProperty("javax.net.ssl.keyStorePassword")) );
        params.put("port", commandLine.getOptionValue("port","443"));
        params.put("protocol",commandLine.hasOption("sslv3")? "SSLv3":null);

        YingmiApiClient ac = new YingmiApiClient(
                params.get("host"),
                Integer.parseInt( params.get("port") ),
                params.get("key"),
                params.get("secret"),
                params.get("keytype"),
                params.get("keystore"),
                params.get("keypass"),
                params.get("protocol")
                );
        
        // invoke the api
        List<FundSearchInfo> funds = ac.getFundsSearchInfo();
        for (FundSearchInfo fund: funds) {
            System.out.println(fund.fundCode);
            System.out.println(fund.fundName);
            System.out.println("============");
        }
        System.out.println(String.format("总共%d只基金", funds.size()));
    }

    private static void showUsage(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("openapi-client",  options);
    }
}
