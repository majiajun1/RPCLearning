package version2.part3.common.Message;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
@Data
@Builder
@Getter
public class RpcRequest implements Serializable {
    private String inferfaceName;  //用动态代理方式 调用接口
    private String methodName;
    private Object[] params;
    private Class<?>[] paramTypes;

}
