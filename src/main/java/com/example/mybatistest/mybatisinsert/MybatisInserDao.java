package com.example.mybatistest.mybatisinsert;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MybatisInserDao {

    int insertInfoTarget(List<Allim> allim);

    int informDelete();

    List<Allim> selectRegisteredPost();

    List<Allim> selectInfoTarget(Allim allim);

    List<Member> listToJson(Member member);

    int fcmJsonInsertGubun(Map<String, String> stringStringMap);

    int fcmInsertUser(Map<String, String> stringStringMap);

    int insertMberTkn(Member member); // 로그인 시 빈 토큰이 들어와 data insert

    int updateMberTkn(Member member); // 로그인 후 name,

    int updateDplcteMberTkn(Member member);

    int fcmGubunInsert(Member member);

    List<Member> fcmListMember(Member member);

    int realInsert(List<Member> fcmListMember);

    int deleteGubunMember(Member member);

    List<Member> fcmSelectSys(Member member);

    List<Member> fcmPushList(Member member);

    int deleteFcmPushList(String member);

    List<Allim> selectArcTarget(Allim allim);

    int updateNtcnSttus(Allim allim);
}
