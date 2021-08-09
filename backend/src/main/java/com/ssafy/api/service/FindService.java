package com.ssafy.api.service;

import com.ssafy.db.entity.board.Board;

import java.util.List;

public interface FindService {
    List<Board> getFindBoardList();

    /* 실종/보호 공고와 유사한 보호/실종 공고 찾기*/
    List<Board> getBoardSimilarListByBoard(Board board);
}
