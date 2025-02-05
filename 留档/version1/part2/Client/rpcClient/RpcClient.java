package version2.part2.Client.rpcClient;

import version2.part2.common.Message.RpcRequest;
import version2.part2.common.Message.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
