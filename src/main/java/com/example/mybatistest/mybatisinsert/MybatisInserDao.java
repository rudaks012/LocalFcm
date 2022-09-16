package com.example.mybatistest.mybatisinsert;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MybatisInserDao {

    int insert(List<Allim> allim);

    int delete();

    List<Allim> select();

    List<Allim> findMembers(Allim allim);

    List<Member> listToJson(Member member);

    int fcmJsonInsertGubun(Map<String, String> stringStringMap);

    int fcmInsertUser(Map<String, String> stringStringMap);

    int fcmInsertPost(Member member); // 로그인 시 빈 토큰이 들어와 data insert

    int fcmUpdatePost(Member member); // 로그인 후 name,

    int fcmDuplicatedTokenUpdate(Member member);

    int fcmGubunInsert(Member member);
}
