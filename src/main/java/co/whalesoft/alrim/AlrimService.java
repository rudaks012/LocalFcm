package co.whalesoft.alrim;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlrimService {

    @Autowired
    private AlrimDao alrimDao;

    public int insertInfoTarget(List<Alrim> alrim) {
        return alrimDao.insertInfoTarget(alrim);
    }

    public int informDelete() {
        return alrimDao.informDelete();
    }

    public List<Alrim> selectRegisteredPost() {
        return alrimDao.selectRegisteredPost();
    }

    public List<Alrim> selectInfoTarget(Alrim alrim) {
        return alrimDao.selectInfoTarget(alrim);
    }

    public List<Alrim> selectArcTarget(Alrim alrim) {
        return alrimDao.selectArcTarget(alrim);
    }

    public int updateNtcnSttus(Alrim alrim) {
        return alrimDao.updateNtcnSttus(alrim);
    }

}
