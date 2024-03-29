/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package CertificateSigningRequest;

import Registration.*;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class CertificateSigningRequestTest {
    @Before
    public void setUp() throws Exception {
        //
        // Disable SSL validation for testing.
        //

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch(Exception ex) {

        }
    }

    @Test public void testCertificateSigningRequest() {
        String WAN_IP = "355808100004297";
        String TOKEN_ID = "cdcae0cc-0813-4a2a-a92b-9937475c2182";
        String aeResourceId = "/Csmartmeter";

        String SIGNING_URI = "https://dev9.corp.aetheros.com:18090/CertificateSigning";
        String CONFIRMING_URI = "https://dev9.corp.aetheros.com:18090/CertificateConfirm";

        try {
            // Create a CA instance with the signing and confirm backend.
            PolicynetCA ca = new PolicynetCA(SIGNING_URI, CONFIRMING_URI);
            // Create a signed certificate.
            CertificateConfirmResponse crr = ca.createCertifcateSigningRequest(WAN_IP, TOKEN_ID, aeResourceId);

            assertNotNull("Confirmed signed certificate contains PEM.", crr.getCaCertPEM());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
    }
}
