package co.whalesoft.push;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PushService {

    @Autowired
    private PushDao pushDao;

    public List<Push> fcmListMember(Push push) {
        return pushDao.fcmListMember(push);
    }

    public int realInsert(List<Push> fcmListPush) {
        return pushDao.realInsert(fcmListPush);
    }

    public List<Push> fcmPushList(Push push) {
        return pushDao.fcmPushList(push);
    }

    public int updatePushSttus(Push sentPushList) {
        return pushDao.updatePushSttus(sentPushList);
    }

    public int deleteFcmUsers() {
        return pushDao.deleteFcmUsers();
    }
    public int deleteFcmNotRegistered(Push push) {
        return pushDao.deleteFcmNotRegistered(push);
    }

    public int deleteManageTable() {
        return pushDao.deleteTwoDayData();
    }

    public List<Push> selectUnsentPushList() {
        return pushDao.selectUnsentPushList();
    }

    public int resetSerial() {
        return pushDao.resetSerial();
    }

    public int insertUnsentPushList(List<Push> unsentPushList) {
        return pushDao.insertUnsentPushList(unsentPushList);
    }

    public int updateFcmPushSttus(int push_sn) {
        return pushDao.updateFcmPushSttus(push_sn);
    }

    public List<Push> selectPushRequestList() {
        return pushDao.selectPushRequestList();
    }

    public List<Push> selectPushSendList() {
        return pushDao.selectPushSendList();
    }

    public void insertPushRequestList(List<Push> pushRequestList) {
        pushDao.insertPushRequestList(pushRequestList);
    }

    public void insertPushSendList(List<Push> pushSendList) {
        pushDao.insertPushSendList(pushSendList);
    }

    public List<Push> fcmResveListMember(Push push) {
        return pushDao.fcmResveListMember(push);
    }

    public List<Push> fcmMberTyResveList(String[] push) {
        return pushDao.fcmMberTyResveList(push);
    }
}
