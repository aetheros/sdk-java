package Registration;

import com.google.gson.Gson;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class PolicynetCA {
    String signingUri;
    String confirmUri;
    Gson gson;

    public PolicynetCA(String signingUri, String confirmUri) {
        this.signingUri = signingUri;
        this.confirmUri = confirmUri;
        this.gson = new Gson();
    }

    public CertificateConfirmResponse createCertifcateSigningRequest(String wanIp, String tokenId, String rsc) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException, OperatorCreationException {
        CertificateSigningResponse csr = this.signCertificate(wanIp, tokenId, rsc);
        CertificateConfirmResponse ccr = this.confirmCertificate(csr);

        return ccr;
    }

    private CertificateSigningResponse signCertificate(String wanIp, String tokenId, String rsc) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException, OperatorCreationException {
        CertificateSigningRequest csr = new CertificateSigningRequest(wanIp, tokenId, rsc);

        HttpURLConnection conn = (HttpURLConnection) new URL(this.signingUri).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] body = csr.toString().getBytes(StandardCharsets.UTF_8);
            os.write(body, 0, body.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            conn.disconnect();

            // Sign cert.
            CertificateSigningResponse certificateSigningResponse = gson.fromJson(response.toString(), CertificateSigningResponse.class);

            return certificateSigningResponse;
        } catch (IOException e) {
            throw e;
        }
    }

    private CertificateConfirmResponse confirmCertificate(CertificateSigningResponse csr) throws IOException {
        CertificateConfirmRequest certificateConfirmRequest = new CertificateConfirmRequest(csr);

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.confirmUri).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try(OutputStream os = conn.getOutputStream()) {
                byte[] body = certificateConfirmRequest.toString().getBytes(StandardCharsets.UTF_8);
                os.write(body, 0, body.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                CertificateConfirmResponse certificateConfirmResponse = gson.fromJson(response.toString(), CertificateConfirmResponse.class);

                return certificateConfirmResponse;
            }
        } catch(IOException e) {
            throw e;
        }
    }
}
