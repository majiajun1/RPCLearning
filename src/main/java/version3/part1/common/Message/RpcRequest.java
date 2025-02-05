package version3.part1.common.Message;

import lombok.*;

import java.io.Serializable;
@Data
@Builder

@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    private String inferfaceName;  //用动态代理方式 调用接口
    private String methodName;
    private Object[] params;
    private Class<?>[] paramsType;

}
