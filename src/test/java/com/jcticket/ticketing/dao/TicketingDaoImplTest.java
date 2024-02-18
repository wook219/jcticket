package com.jcticket.ticketing.dao;

import com.jcticket.admin.dao.AdminDao;
import com.jcticket.ticketing.dto.TicketingDto;
import com.jcticket.viewdetail.dto.ShowingDto;
import org.apache.ibatis.jdbc.Null;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

/**
 * packageName :  com.jcticket.ticketing.dao
 * fileName : TicketingDaoImplTest
 * author :  jisoo Son
 * date : 2024-02-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2024-02-10             jisoo Son             최초 생성
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TicketingDaoImplTest {

    final Timestamp NOW = new Timestamp(System.currentTimeMillis());
    String SYS = "system";
    String testPlay_id = "테스트공연ID";
    String testStage_id = "테스트공연장ID";
    String endDate = "2024-02-20 23:59:59";
    String testDate = "2024-02-24";
    @Autowired
    TicketingDao ticketingDao;

/*
    @Before
    public void deleteAll() throws Exception{
        int result = ticketingDao.deleteAll();
    }
 */

    @Test
    public void insertTest() throws Exception{
        // given -> 임의의 데이터를 주입
        ShowingDto dto = new ShowingDto("1회 09시 30분", testDate, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
        // when -> 데이터의 조건 dao, service
        int result = ticketingDao.insert(dto);
        // then -> 검증하는 절차
        assertTrue(1 == result);
    }
    @Test
    public void deleteByPlayIdTest() throws Exception {
        //given
        ShowingDto dto = new ShowingDto("2회 12시 30분", testDate, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
        ticketingDao.insert(dto);
        //when
        int result = ticketingDao.deleteByPlayId(testPlay_id);
        //then
        assertEquals(1, result);
    }

    // 공연아이디별 회차 수 카운트
    @Test
    public void countTest() throws Exception{
        //given
        for(int i = 1; i <= 10; i++){
            ShowingDto dto = new ShowingDto(i+"회 "+i+"시 00분", testDate, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
            ticketingDao.insert(dto);
        }
        //when
        int result = ticketingDao.countByPlayId(testPlay_id);
        //then
        assertTrue(result == 10);
    }

    // 공연아이디별 조회 테스트
    public void selectByPlayIdTest() throws Exception{
        //given
        for(int i = 1; i <= 10; i++){
            ShowingDto dto = new ShowingDto(i+"회 "+i+"시 00분", testDate, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
            ticketingDao.insert(dto);
        }
        //when
        List<ShowingDto> list = ticketingDao.selectByPlayId(testPlay_id);
        //then
        for(ShowingDto dto : list){
            assertEquals(testPlay_id, dto.getPlay_id());
            assertEquals(testDate, dto.getShowing_date());
            assertEquals(testStage_id, dto.getStage_id());
        }
    }

    // 공연아이디별 공연일정 조회
    public void selectDateTest() throws Exception{
        //given
        for(int i = 1; i <= 5; i++){
            ShowingDto dto = new ShowingDto(i+"회 "+i+"시 00분", testDate, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
            ticketingDao.insert(dto);
        }
        //when
        List<Map<String,String>> list = ticketingDao.selectDateByPlayId(testPlay_id);
        //then
        assertTrue(list.size() == 5);
        for(Map<String,String> map : list) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                if (key.equals("showing_date")) {
                    assertEquals(testDate, map.get(key));
                }
            }
        }
    }

    // 공연아이디 & 공연일정별 회차 시퀀스 회차정보 리스트 조회
    @Test
    public void selectRoundTest() throws Exception{
        //given
        String tDate = "2024-02-";
        for (int i = 1; i <= 10 ; i++) {
            ShowingDto dto;
            if(i == 10){
                dto = new ShowingDto(i + "회 " + i + "시 00분", tDate + i, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
            }else {
                dto = new ShowingDto(i + "회 " + i + "시 00분", tDate + "0" + i, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
            }
            ticketingDao.insert(dto);
        }
        Map<String,String> param = new HashMap<>();
        param.put("play_id",testPlay_id);
        param.put("showing_date","2024-02-01");
        //when
        List<Map<String,Object>> list = ticketingDao.selectRound(param);
        //then
        for(Map<String,Object> map : list){
            Set<String> keys = map.keySet();
            for(String key : keys){
                if(key.equals("showing_seq")){
                    assertTrue(map.get(key) != null);
                } else if (key.equals("showing_info")){
                    assertEquals("1회 1시 00분", map.get(key));
                }
            }
        }
    }

    // 공연아이디별 공연명, 공연장명 조회(단일 행 반환)
    @Test
    public void selectPlayStageNameTest() throws Exception{
        //given
        for(int i=1; i <= 5; i++){
            ShowingDto dto1 = new ShowingDto(i+"회 "+i+"시 00분", testDate, "토", "BS", 80, testPlay_id+"1", testStage_id, SYS, SYS);
            ShowingDto dto2 = new ShowingDto(i+"회 "+i+"시 00분", testDate, "토", "BS", 80, testPlay_id+"2", testStage_id, SYS, SYS);
            ticketingDao.insert(dto1);
            ticketingDao.insert(dto2);
        }
        //when
        Map<String,String> map = ticketingDao.selectPlayStageName(testPlay_id+"1");
        //then
        Set<String> keys = map.keySet();
        for(String key : keys){
            if (key.equals("play_name"))
                assertEquals("테스트공연", map.get(key));
            else if(key.equals("stage_name"))
                assertEquals("정석극장", map.get(key));
        }
    }
}