package version1.part2.Client.rpcClient;

import version1.part2.common.Message.RpcRequest;
import version1.part2.common.Message.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
