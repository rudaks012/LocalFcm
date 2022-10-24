package co.whalesoft.alrim;

import co.whalesoft.push.Push;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AlrimDao {

    int insertInfoTarget(List<Alrim> alrim);

    int informDelete();

    List<Alrim> selectRegisteredPost();

    List<Alrim> selectInfoTarget(Alrim alrim);

    List<Alrim> selectArcTarget(Alrim alrim);

    int updateNtcnSttus(Alrim alrim);

}
