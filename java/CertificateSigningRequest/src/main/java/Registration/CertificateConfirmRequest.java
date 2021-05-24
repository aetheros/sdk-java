package Registration;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class CertificateConfirmRequest {
    @SerializedName(value="pnm2m:confirmreq", alternate ="confirmreq")
    ConfirmRequest confirmreq;

    public CertificateConfirmRequest(CertificateSigningResponse certificateSigningResponse) {
        this.confirmreq = new ConfirmRequest(certificateSigningResponse);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    private class ConfirmRequest {
        String certhash;
        CertId certid;
        String txnid;

        public ConfirmRequest(CertificateSigningResponse certificateSigningResponse) {
            this.txnid = certificateSigningResponse.getConfirmTxnid();

            try {
                // Read x509 certificate.
                CertificateFactory factory = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream bytes = new ByteArrayInputStream(certificateSigningResponse.getClientCert().toString().getBytes(StandardCharsets.UTF_8));
                X509Certificate cert = (X509Certificate) factory.generateCertificate(bytes);

                this.certid = new CertId(
                        cert.getIssuerDN().toString(),
                        cert.getSerialNumber()
                );

                this.certhash = certificateSigningResponse.getCertHash("SHA-256");
            } catch(Exception ex) {
                // handle it.
            }
        }

        private class CertId {
            String issuer;
            BigInteger serial;

            public CertId(String issuer, BigInteger serial) {
                this.issuer = issuer;
                this.serial = serial;
            }
        }
    }
}
