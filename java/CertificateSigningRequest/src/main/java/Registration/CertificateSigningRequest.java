package Registration;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import java.security.*;
import java.io.*;
import java.security.spec.ECGenParameterSpec;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;


public class CertificateSigningRequest {
    /* {
     *      pnm2m:signreq: {
     *
     *      }
     */
    @SerializedName(value = "pnm2m:signreq", alternate = "signreq")
    SigningRequest signreq;


    public CertificateSigningRequest(String wanaddr, String tokenid) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException, OperatorCreationException {
        this.signreq = new SigningRequest(
                wanaddr,
                tokenid,
                this.getRequestCert(wanaddr)
        );
    }

    public SigningRequest getSignreq() {
        return signreq;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    private String getRequestCert(String wanaddr) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, OperatorCreationException, IOException {
        // Generate key pair.

        // RSA
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(4096);

        // ECDSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
        KeyPair keypair = keyGen.genKeyPair();

        // Build subject.
        X500Principal subject = new X500Principal("C = US, ST = California, O = Grid Net, OU = Engineering, CN = Grid-Net-Dev-PolicyNet-Root-CA");

        // Build the CSR.
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subject, keypair.getPublic());

        GeneralName[] san = new GeneralName[]{
                new GeneralName(GeneralName.dNSName, wanaddr)
        };

        Extension[] extensions = new Extension[] {
                Extension.create(Extension.subjectAlternativeName, true, new GeneralNames(san))
        };

        p10Builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, new Extensions(extensions));

        // RSA
//        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");

        // ECDSA
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withECDSA");
        ContentSigner signer = csBuilder.build(keypair.getPrivate());
        PKCS10CertificationRequest csr = p10Builder.build(signer);

        // Write PEM format.
        final StringWriter writer = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(csr);
        pemWriter.flush();
        pemWriter.close();

        return writer.toString();
    }

    /*
     * {
     *  device: {
     *
     * }
     */
    private class SigningRequest {
        Device device;
        String xcsr;

        public SigningRequest(String wanaddr, String tokenid, String xcsr) {
            this.device = new Device(wanaddr, tokenid);
            this.xcsr = xcsr;
        }

        public String getWanaddr() {
            return this.device.getWanaddr();
        }

        public String getTokenid() {
            return this.device.getTokenid();
        }

        private class Device {
            String wanaddr;
            String tokenid;

            public Device(String wanaddr, String tokenid) {
                this.wanaddr = wanaddr;
                this.tokenid = tokenid;
            }

            public String getWanaddr() {
                return wanaddr;
            }

            public String getTokenid() {
                return tokenid;
            }
        }
    }
}
