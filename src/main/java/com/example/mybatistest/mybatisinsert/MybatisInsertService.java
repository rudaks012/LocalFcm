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

    public int insert(List<Allim> allim) {
        return mybatisInserDao.insert(allim);
    }

    public int delete() {
        return mybatisInserDao.delete();
    }

    public List<Allim> select() {
        return mybatisInserDao.select();
    }

    public List<Allim> findMembers(Allim allim) {
        return mybatisInserDao.findMembers(allim);
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

    public int fcmInsertPost(Member member) {

        return mybatisInserDao.fcmInsertPost(member);
    }

    public int fcmUpdatePost(Member member) {
        return mybatisInserDao.fcmUpdatePost(member);
    }

    public int fcmDuplicatedTokenUpdate(Member member) {
        return mybatisInserDao.fcmDuplicatedTokenUpdate(member);
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
}
