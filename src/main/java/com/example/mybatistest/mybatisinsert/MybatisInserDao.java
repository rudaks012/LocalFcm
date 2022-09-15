package com.example.mybatistest.mybatisinsert;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MybatisInserDao {

    public int insert(List<Allim> allim);

    public int delete();

    public List<Allim> select();

    public List<Allim> findMembers(Allim allim);

    public List<Member> listToJson(Member member);

    public int fcmJsonInsertGubun(Map<String, String> stringStringMap);

    public int fcmInsertUser(Map<String, String> stringStringMap);

    public int fcmInsertPost(Member member);

}
