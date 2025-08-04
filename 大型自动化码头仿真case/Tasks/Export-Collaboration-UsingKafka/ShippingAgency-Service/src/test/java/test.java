import com.forestlake.workflow.ShippingAgencyServices.utils.PostMethodUtils;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        PostMethodUtils.sendPost("Message_CB_Manifest_received", "a-key", "customs");
    }
}
