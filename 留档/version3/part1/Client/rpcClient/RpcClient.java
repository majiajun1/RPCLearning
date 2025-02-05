package version3.part1.Client.rpcClient;

import version3.part1.common.Message.RpcRequest;
import version3.part1.common.Message.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
