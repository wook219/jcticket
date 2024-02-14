package com.jcticket.viewdetail.dao;

import com.jcticket.notice.dto.NoticeDto;
import com.jcticket.viewdetail.dto.ShowingDto;
import com.jcticket.viewdetail.service.ViewDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * packageName    : com.jcticket.viewdetail.dao
 * fileName       : ViewDetailDaoTest
 * author         : sungjun
 * date           : 2024-02-11
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-11        kyd54       최초 생성
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class ViewDetailDaoTest {

    @Autowired
    ViewDetailDao viewDetailDao;

}