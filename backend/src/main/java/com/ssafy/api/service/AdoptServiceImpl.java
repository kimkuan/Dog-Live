package com.ssafy.api.service;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssafy.api.request.AdoptFormReq;
import com.ssafy.db.entity.auth.CounselingHistory;
import com.ssafy.db.entity.auth.User;
import com.ssafy.db.entity.auth.UserProfile;
import com.ssafy.db.entity.board.Board;
import com.ssafy.db.repository.auth.CounselingHistoryRepository;
import com.ssafy.db.repository.auth.UserProfileRepository;
import com.ssafy.db.repository.auth.UserRepository;
import com.ssafy.db.repository.board.*;

import io.micrometer.core.instrument.util.JsonUtils;
import org.checkerframework.checker.nullness.Opt;
import org.h2.util.json.JSONObject;
import org.h2.util.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service("adoptService")
public class AdoptServiceImpl implements AdoptService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    CounselingHistoryRepository counselingHistoryRepository;

    @Autowired
    BoardRepository boardRepository;


    /* 입양임보 게시물 전체 목록 불러오기 */
    @Override
    public List<Board> getAdoptBoardList() {
        Optional<List<Board>> boardList = boardRepository.findAdoptBoard();
        if(boardList.isPresent()) return boardList.get();

        return null;
    }

    /* 입양신청서 작성하기 */
    @Override
    public CounselingHistory insertAdoptForm(String userId, AdoptFormReq adoptFormReq) {

        CounselingHistory counselingHistory = new CounselingHistory();

        JsonParser parser = new JsonParser();
        System.out.println(adoptFormReq.getContent());

        UserProfile userProfile = findByUserId(userId);
        if(userProfile!=null) {
            counselingHistory.setApplicantId(userProfile);
        }

        Long boardId = adoptFormReq.getBoardId();
        counselingHistory.setBoardId(boardId);
        counselingHistory.setBoardType(adoptFormReq.getBoardType());
        counselingHistory.setDogName(adoptFormReq.getDogName());
        counselingHistory.setContent(adoptFormReq.getContent().toString());
        counselingHistory.setResult("대기");
        counselingHistory.setWriter(boardRepository.findById(boardId).get().getUserId());

        counselingHistoryRepository.save(counselingHistory);


        return counselingHistory;
    }

    @Override
    public UserProfile findByUserId(String userId) {
        Optional<User> user = userRepository.findUserById(userId);
        if(user.isPresent()) {
            Optional<UserProfile> userProfile = userProfileRepository.findByUserId(user.get());
            if (userProfile.isPresent()) {
                return userProfile.get();
            }
        }
        return null;
    }

    @Override
    public boolean canAdoptForm(String userId, Long boardId) {

        UserProfile userProfile = findByUserId(userId);
        if(userProfile!=null) {
            Optional<CounselingHistory> counselingHistories =
                    counselingHistoryRepository.
                            findCounselingHistoryByApplicantIdAndBoardId(userProfile, boardId);

            if(counselingHistories.isPresent()){
                 return false;
            }
        }

        return true;
    }


}
