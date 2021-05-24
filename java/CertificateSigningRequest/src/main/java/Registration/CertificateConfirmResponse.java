package Registration;

import com.google.gson.annotations.SerializedName;

public class CertificateConfirmResponse {
    @SerializedName(value="pnm2m:confirmresp", alternate ="confirmresp")
    ConfirmResponse confirmresp;

    public String getCaCertPEM() {
        return this.confirmresp.cacertpem;
    }

    public String getNewTokenId() {
        return this.confirmresp.newtokenid;
    }

    public int getStatus() {
        return this.confirmresp.status;
    }

    public class ConfirmResponse {
       String cacertpem;
       String newtokenid;
       int status;
    }
}
