package version2.part1.Client.rpcClient;

import version2.part1.common.Message.RpcRequest;
import version2.part1.common.Message.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
