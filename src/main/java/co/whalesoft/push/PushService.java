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

    public int insertMberTkn(Push push) {
        return pushDao.insertMberTkn(push);
    }

    public int updateMberTkn(Push push) {
        return pushDao.updateMberTkn(push);
    }

    public int updateDplcteMberTkn(Push push) {
        return pushDao.updateDplcteMberTkn(push);
    }

    public int fcmGubunInsert(Push push) {
        return pushDao.fcmGubunInsert(push);
    }

    public List<Push> fcmListMember(Push push) {
        return pushDao.fcmListMember(push);
    }

    public int realInsert(List<Push> fcmListPush) {
        return pushDao.realInsert(fcmListPush);
    }

    public int deleteGubunMember(Push push) {
        return pushDao.deleteGubunMember(push);
    }

    public List<Push> fcmSelectSys(Push push) {
        return pushDao.fcmSelectSys(push);
    }

    public List<Push> fcmPushList(Push push) {
        return pushDao.fcmPushList(push);
    }

    public int deleteFcmPushList(String token) {
        return pushDao.deleteFcmPushList(token);
    }

    public int updatePushSttus(Push sentPushList) {
        return pushDao.updatePushSttus(sentPushList);
    }
}