package ${package.ServiceImpl};

import com.venble.boot.jpa.service.QueryService;
import ${package.Entity}.${entity};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${table.serviceImplName} extends QueryService<${entity}>{

}
