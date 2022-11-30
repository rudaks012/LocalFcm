package co.whalesoft.push;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PushDao {

    List<Push> fcmListMember(Push push);

    int realInsert(List<Push> fcmListPush);

    List<Push> fcmPushList(Push push);

    int updatePushSttus(Push push);

    int deleteFcmUsers();

    int deleteFcmNotRegistered(Push push);

    int deleteTwoDayData();

    List<Push> selectUnsentPushList();

    int resetSerial();

    int insertUnsentPushList(List<Push> unsentPushList);

    int updateFcmPushSttus(int push_sn);

    List<Push> selectPushRequestList();

    List<Push> selectPushSendList();

    void insertPushRequestList(List<Push> pushRequestList);

    void insertPushSendList(List<Push> pushSendList);
}
