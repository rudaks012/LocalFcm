package co.whalesoft.push;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PushDao {

    int insertMberTkn(Push push); // 로그인 시 빈 토큰이 들어와 data insert

    int updateMberTkn(Push push); // 로그인 후 name,

    int updateDplcteMberTkn(Push push);

    int fcmGubunInsert(Push push);

    List<Push> fcmListMember(Push push);

    int realInsert(List<Push> fcmListPush);

    int deleteGubunMember(Push push);

    List<Push> fcmPushList(Push push);

    int updatePushSttus(Push push);

    int deleteFcmUsers(Push sentPushList);

    int deleteFcmNotRegistered(Push push);

    int deleteTwoDayData();

    List<Push> selectUnsentPushList();

    int resetSerial();
}
