package ${package.Mapper};

import ${package.Entity}.${entity};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * ${table.comment!} Repository 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Repository
public interface ${table.mapperName} extends JpaRepository<${entity}, Long>, JpaSpecificationExecutor<${entity}>  {

}

