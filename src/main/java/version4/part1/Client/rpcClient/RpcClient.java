package version4.part1.Client.rpcClient;

import version4.part1.common.Message.RpcRequest;
import version4.part1.common.Message.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
