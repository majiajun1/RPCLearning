package version2.part3.Client.rpcClient;

import version2.part3.common.Message.RpcRequest;
import version2.part3.common.Message.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
