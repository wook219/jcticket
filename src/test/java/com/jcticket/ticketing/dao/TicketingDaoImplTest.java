package com.jcticket.ticketing.dao;

import com.jcticket.admin.dao.AdminDao;
import com.jcticket.admin.dto.ShowSeatDto;
import com.jcticket.dto.SeatDto;
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
    String testPlay_id = "6f72bd86";
    String testStage_id = "1ad62b31";
    String endDate = "2024-02-20 23:59:59";
    String testDate = "2024-02-24";

    @Autowired
    TicketingDao ticketingDao;

//    @Before
//    public void init() throws Exception {
//        System.out.println("init DELETE ALL");
//        ticketingDao.deleteAll();
//    }

    @Test
    public void insertTest() throws Exception {
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
    public void countTest() throws Exception {
        //given
        for (int i = 1; i <= 10; i++) {
            ShowingDto dto = new ShowingDto(i + "회 " + i + "시 00분", testDate, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
            ticketingDao.insert(dto);
        }
        //when
        int result = ticketingDao.countByPlayId(testPlay_id);
        //then
        assertTrue(result == 10);
    }

    // 공연아이디별 조회 테스트
    @Test
    public void selectByPlayIdTest() throws Exception {
        //given
        for (int i = 1; i <= 10; i++) {
            ShowingDto dto = new ShowingDto(i + "회 " + i + "시 00분", testDate, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
            ticketingDao.insert(dto);
        }
        //when
        List<ShowingDto> list = ticketingDao.selectByPlayId(testPlay_id);
        //then
        for (ShowingDto dto : list) {
            assertEquals(testPlay_id, dto.getPlay_id());
            assertEquals(testDate, dto.getShowing_date());
            assertEquals(testStage_id, dto.getStage_id());
        }
    }

    // 공연아이디별 공연일정 조회
    @Test
    public void selectDateTest() throws Exception {
        //given

        for (int i = 1; i <= 5; i++) {
            ShowingDto dto = new ShowingDto(i + "회 " + i + "시 00분", testDate, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);

            ticketingDao.insert(dto);
            System.out.println("dto"+i+" ==> "+dto.toString());
        }
        //when
        List<Map<String, String>> list = ticketingDao.selectDateByPlayId(testPlay_id);
        //then
        System.out.println("list size ==> " + list.size());
        assertTrue(list.size() == 5);
        for (Map<String, String> map : list) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                System.out.println("key ==> " + key);
                System.out.println("value ==> " + map.get(key));
                if (key.equals("showing_date")) {
                    assertEquals(testDate, map.get(key));
                }
            }
        }
    }

    // 공연아이디 & 공연일정별 회차정보 리스트 조회
    @Test
    public void selectRoundTest() throws Exception {
        //given
        String tDate = "2024-02-2";
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 3; j++) {
                ShowingDto dto = new ShowingDto(j + "회 " + j + "시 00분", tDate + i, "토", "BS", 80, testPlay_id, testStage_id, SYS, SYS);
                ticketingDao.insert(dto);
            }
        }
        int r = 1;
        Map<String, String> param = new HashMap<>();
        param.put("play_id", testPlay_id);
        param.put("date_text", "2024-02-21");
        //when
        List<Map<String, Object>> list = ticketingDao.selectRound(param);
        //then
        System.out.println("list size ==> " + list.size());
        assertTrue(3 == list.size());
        for (Map<String, Object> map : list) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                if (key.equals("showing_seq")) {
                    System.out.println("showing_seq ==> " + map.get(key));
                    assertTrue(map.get(key) != null);
                } else if (key.equals("showing_info")) {
                    System.out.println("showing_info ==> " + map.get(key));
                    assertEquals(r + "회 " + r + "시 00분", map.get(key));
                }
            }
            r++;
        }
    }

    // 공연아이디별 공연명, 공연포스터, 공연장명 조회(단일 행 반환)
    @Test
    public void selectPlayStageNameTest() throws Exception {
        //given
        for (int i = 1; i <= 5; i++) {
            ShowingDto dto1 = new ShowingDto(i + "회 " + i + "시 00분", testDate, "토", "BS", 80, testPlay_id + "1", testStage_id, SYS, SYS);
            ShowingDto dto2 = new ShowingDto(i + "회 " + i + "시 00분", testDate, "토", "BS", 80, testPlay_id + "2", testStage_id, SYS, SYS);
            ticketingDao.insert(dto1);
            ticketingDao.insert(dto2);
        }
        //when
        Map<String, Object> map = ticketingDao.selectPlayStageName(testPlay_id + "1");
        //then
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (key.equals("play_name")) {
                System.out.println("공연명 ==> " + map.get(key));
                assertEquals("테스트공연", map.get(key));
            } else if (key.equals("play_poster")) {
                System.out.println("공연장 포스터 ==> " + map.get(key));
                assertTrue(map.get(key) != null);
            } else if (key.equals("stage_name")) {
                System.out.println("공연장명 ==> " + map.get(key));
                assertEquals("정석극장", map.get(key));
            }

        }
    }
    // 회차좌석 테이블 insert
    @Test
    public void insertTest2() throws Exception{
        final int SEQ = 17;
        String stageID = "1ad62b31";
        int seatCnt = ticketingDao.selectSeatCnt(SEQ);
        final int COL = 10;
        final int ROW = seatCnt / COL;

        //회차 좌석테이블 dto 생성
        ShowSeatDto dto = new ShowSeatDto();

        System.out.println("========== 좌석 수 검증 시작 =========");
        System.out.println("좌석 수 => "+ seatCnt);
        System.out.println("행 개수 => " + ROW);
        System.out.println("열 개수 => "+ COL);
        int res = 0;

        // dto에 insert 하기
        for(int i = 1; i <= ROW; i++){
            for(int j = 1; j <= COL; j++){
                char row = (char)(64+i);
                String seat = String.format("%c%d", row, j);
                System.out.print(seat + " ");
                dto.setShowing_seq(SEQ);
                dto.setSeat_row(String.valueOf(row));
                dto.setSeat_col(j);
                dto.setStage_id(stageID);
                res = ticketingDao.insertShowSeat(dto);
                assertEquals(1, res);
            }
            System.out.println();
        }
        System.out.println("========= 좌석 수 검증 끝 =========");
    }

    // 회차좌석 테이블 삭제
    @Test
    public void deleteShowSeat() throws Exception{
        int res = ticketingDao.deleteShowSeat();
        assertEquals(80*6, res);
    }

    // 좌석테이블 삽입
    @Test
    public void insertSeatTest() throws Exception{
        String stage_id = "5ad62b31";
        final int SEAT_CNT = 40;
        final int COL = 10;

        int rowNum = SEAT_CNT / COL;
        for(int i = 0; i < rowNum; i++){
            String row = String.valueOf((char)('A' + i));
            for(int j = 0; j < COL; j++){
                SeatDto dto = new SeatDto(row, j+1, stage_id);
                int res = ticketingDao.insertSeat(dto);
                assertTrue(1 == res);
            }
        }
    }
    // 좌석테이블 공연장아이디별 삭제
    @Test
    public void deleteSeatByStageIdTest() throws Exception{
        String stage_id = "5ad62b31";
        System.out.println(stage_id);
        int res = ticketingDao.deleteSeatByStageId(stage_id);
       System.out.println("res => "+res);
        //assertTrue(1 == res);
    }
    // 회차시퀀스별 회차좌석가격 조회
    //
    @Test
    public void selectPriceTest() throws Exception{
        // given
        // 시퀀스를 설정한다.
        final int SEQ = 2;
        int expected = 15000;
        int result;
        // when
        result = ticketingDao.selectPrice(SEQ);
        System.out.println("sequence => " + SEQ);
        System.out.println("expected => " + expected);
        System.out.println("result => "+ result);
        // then
        assertEquals(expected, result);
    }

    // 회차시퀀스별 좌석번호, 좌석상태리스트 조회
    @Test
    public void selectSeatListTest() throws Exception{
        // given
        // 시퀀스를 설정한다.
        final int SEQ = 1;
        String expectedKey1 = "seat_id";
        String expectedKey2 = "seat_status";
        List<Map<String, String>> list;
        // when
        list = ticketingDao.selectSeatList(SEQ);
        // then
        for (Map<String, String> map : list){
            Set<String> keys = map.keySet();
            for (String key : keys){
                if(key.equals(expectedKey1)){
                    assertEquals(expectedKey1, key);
                    assertTrue(null != map.get(key));
                    System.out.println("key => " + key);
                    System.out.println("value => " + map.get(key));
                }
                else {
                    assertEquals(expectedKey2, key);
                    assertTrue(null != map.get(key));
                    System.out.println("key => " + key);
                    System.out.println("value => " + map.get(key));
                }
            }
        }
    }

    // 회차시퀀스로 회차좌석의 행, 열의 마지막 번호 구하기
    @Test
    public void selectEndNum() throws Exception{
        //given
        final int SEQ = 10;
        int expectedCol = 10;
        int resultCol = 0;
        String expectedRow = "H";
        String resultRow = "";
        Map<String, Object> map;
        //when
        map = ticketingDao.selectEndNum(SEQ);
        System.out.println("map => "+ map);
        System.out.println(map.getClass().getName());

        //then
        Set<String> keys = map.keySet();
        System.out.println(keys);
        for(String key : keys){
            if(key.equals("end_col")){
                System.out.println("end_col => "+ map.get(key));
                System.out.println("type => " + map.get(key).getClass().getName());
                resultCol = (int) map.get(key);
            }else {
                System.out.println("end_row => "+ map.get(key));
                System.out.println("type => " + map.get(key).getClass().getName());
                resultRow = (String) map.get(key);
            }
        }
        assertEquals(expectedCol, resultCol);
        assertEquals(expectedRow, resultRow);
    }
}