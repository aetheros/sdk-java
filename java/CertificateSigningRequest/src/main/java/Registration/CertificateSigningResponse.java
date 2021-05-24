package Registration;

import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CertificateSigningResponse {
    /*
     *
     * {
     *      pnm2m:signresp: {
     *
     *      }
     * }
     */
    @SerializedName(value="pnm2m:signresp", alternate="signresp")
    SigningResponse signresp;

    public CertificateSigningResponse() {
    }

    public String getStatus() {
        return this.signresp.status;
    }

    public String getClientCert() {
        return this.signresp.clientcert;
    }

    public String getConfirmTxnid() {
        return this.signresp.confirmtxnid;
    }

    public String getFailInfo() {
        return this.signresp.failinfo;
    }

    public String getInfoMsg() {
        return this.signresp.infomsg;
    }

    public String getCertHash(String hash) throws NoSuchAlgorithmException, CertificateException {
        try {
            MessageDigest digest = MessageDigest.getInstance(hash);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(this.signresp.clientcert.getBytes(StandardCharsets.UTF_8)));
            byte[] hashedCert = digest.digest(cert.getEncoded());
            String b64EncodedCert = Base64.getEncoder().encodeToString(hashedCert);

            return b64EncodedCert;
        } catch(NoSuchAlgorithmException | CertificateException ex) {
            throw ex;
        }
    }

    public class SigningResponse {
        String status;
        String clientcert;
        String confirmtxnid;
        String failinfo;
        String infomsg;

        public SigningResponse() {
            // Instantiation handled by Gson.
        }
    }
}
