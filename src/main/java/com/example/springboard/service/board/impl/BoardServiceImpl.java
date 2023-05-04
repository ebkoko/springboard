package com.example.springboard.service.board.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.springboard.dto.BoardDTO;
import com.example.springboard.dto.BoardFileDTO;
import com.example.springboard.mapper.BoardFileMapper;
import com.example.springboard.mapper.BoardMapper;
import com.example.springboard.service.board.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BoardFileMapper boardFileMapper;
	
	@Override
	public void insertBoard(BoardDTO boardDTO, List<BoardFileDTO> uploadFileList) {
		boardMapper.insertBoard(boardDTO);
		
		System.out.println(boardDTO.getBoardNo());
		for(BoardFileDTO boardFile : uploadFileList) {
			boardFile.setBoardNo(boardDTO.getBoardNo());
			
			int fileNo = boardFileMapper.getMaxFileNo(boardDTO.getBoardNo());
			boardFile.setFileNo(fileNo);
			System.out.println(boardDTO.getBoardNo());
			boardFileMapper.insertBoardFile(boardFile);
		}
	}
	
	@Override
	public List<BoardDTO> getBoardList(BoardDTO boardDTO) {
		return boardMapper.getBoardList(boardDTO);
	}
	
	@Override
	public void updateBoardCnt(int boardNo) {
		boardMapper.updateBoardCnt(boardNo);
	}
	
	@Override
	public BoardDTO getBoard(int boardNo) {
		return boardMapper.getBoard(boardNo);
	}
	
	@Override
	public void deleteBoard(int boardNo) {
		boardMapper.deleteBoard(boardNo);
	}
	
	@Override
	public void updateBoard(BoardDTO boardDTO) {
		boardMapper.updateBoard(boardDTO);
	}
	
	@Override
	public List<BoardFileDTO> getBoardFileList(int boardNo) {
		BoardDTO board = BoardDTO.builder()
								 .boardNo(boardNo)
								 .build();
		
		return boardFileMapper.getBoardFileList(board);
	}
	
/*	@Override
	public Page<BoardDTO> getPageBoardList(BoardDTO boardDTO, Pageable pageable) {
		if(boardDTO.getSearchKeyword() != null && !boardDTO.getSearchKeyword().equals("")) {
			if(boardDTO.getSearchCondition().equals("ALL")) {
				return boardMapper
			}
		}
	} */
}
