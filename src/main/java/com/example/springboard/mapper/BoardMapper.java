package com.example.springboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import com.example.springboard.dto.BoardDTO;

@Mapper
public interface BoardMapper {
	@SelectKey(
			statementType=StatementType.PREPARED,
			statement="SELECT IFNULL(MAX(B.BOARD_NO), 0)+1 FROM T_SB_BOARD B",
			keyProperty="boardNo",
			before=true,
			resultType=int.class
			)
	@Insert("INSERT INTO T_SB_BOARD ("
			+ "BOARD_NO,"
			+ "CATE_NO,"
			+ "BOARD_TITLE,"
			+ "BOARD_CONTENT,"
			+ "BOARD_WRITER,"
			+ "BOARD_REGDATE,"
			+ "BOARD_CNT"
			+ ") VALUES ("
			+ "#{boardNo},"
			+ "#{cateNo},"
			+ "#{boardTitle},"
			+ "#{boardContent},"
			+ "#{boardWriter},"
			+ "NOW(),"
			+ "0)") 
	void insertBoard(BoardDTO boardDTO);
	
	@Select("SELECT * FROM T_SB_BOARD")
	List<BoardDTO> getBoardList(BoardDTO boardDTO);
	
	@Update("UPDATE T_SB_BOARD"
			+ " SET BOARD_CNT = BOARD_CNT + 1"
			+ " WHERE BOARD_NO = #{boardNo}") 
	void updateBoardCnt(int boardNo);
	
	@Select("SELECT * FROM T_SB_BOARD WHERE BOARD_NO = #{boardNo}")
	BoardDTO getBoard(int boardNo);
	
	@Delete("DELETE FROM T_SB_BOARD WHERE BOARD_NO = #{boardNo}")
	void deleteBoard(int boardNo);
	
	@Update("UPDATE T_SB_BOARD"
			+ " SET BOARD_TITLE = #{boardTitle},"
			+ " 	BOARD_CONTENT = #{boardContent}"
			+ " WHERE BOARD_NO = #{boardNo}") 
	void updateBoard(BoardDTO boardDTO);
	
	
}
