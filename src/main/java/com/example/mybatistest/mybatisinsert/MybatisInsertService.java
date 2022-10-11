package com.example.mybatistest.mybatisinsert;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MybatisInsertService {

    @Autowired
    private MybatisInserDao mybatisInserDao;

    public int insertInfoTarget(List<Allim> allim) {
        return mybatisInserDao.insertInfoTarget(allim);
    }

    public int informDelete() {
        return mybatisInserDao.informDelete();
    }

    public List<Allim> selectRegisteredPost() {
        return mybatisInserDao.selectRegisteredPost();
    }

    public List<Allim> selectInfoTarget(Allim allim) {
        return mybatisInserDao.selectInfoTarget(allim);
    }

    public List<Member> listToJson(Member member) {
        return mybatisInserDao.listToJson(member);
    }

    public void fcmJsonInsertGubun(Map<String, String> stringStringMap) {
        mybatisInserDao.fcmJsonInsertGubun(stringStringMap);
    }

    public int fcmInsertUser(Map<String, String> stringStringMap) {

        return mybatisInserDao.fcmInsertUser(stringStringMap);
    }

    public int insertMberTkn(Member member) {
        return mybatisInserDao.insertMberTkn(member);
    }

    public int updateMberTkn(Member member) {
        return mybatisInserDao.updateMberTkn(member);
    }

    public int updateDplcteMberTkn(Member member) {
        return mybatisInserDao.updateDplcteMberTkn(member);
    }

    public int fcmGubunInsert(Member member) {
        return mybatisInserDao.fcmGubunInsert(member);
    }

    public List<Member> fcmListMember(Member member) {
        return mybatisInserDao.fcmListMember(member);
    }

    public int realInsert(List<Member> fcmListMember) {
        return mybatisInserDao.realInsert(fcmListMember);
    }

    public int deleteGubunMember(Member member) {
        return mybatisInserDao.deleteGubunMember(member);
    }

    public List<Member> fcmSelectSys(Member member) {
        return mybatisInserDao.fcmSelectSys(member);
    }

    public List<Member> fcmPushList(Member member) {
        return mybatisInserDao.fcmPushList(member);
    }

    public int deleteFcmPushList(String token) {
        return mybatisInserDao.deleteFcmPushList(token);
    }

    public List<Allim> selectArcTarget(Allim allim) {
        return mybatisInserDao.selectArcTarget(allim);
    }

    public int updateNtcnSttus(Allim allim) {
        return mybatisInserDao.updateNtcnSttus(allim);
    }
}
