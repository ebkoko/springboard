package com.example.springboard.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.springboard.common.FileUtils;
import com.example.springboard.dto.BoardDTO;
import com.example.springboard.dto.BoardFileDTO;
import com.example.springboard.service.board.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	// 게시판 목록
	@GetMapping("/boardList")
	public ModelAndView getBoardList(BoardDTO boardDTO/*,
			@PageableDefault(page = 0, size = 10) Pageable pageable*/) {
		List<BoardDTO> boardList = boardService.getBoardList(boardDTO);
		
/*		Page<BoardDTO> pageBoardList = boardService.getPageBoardList(boardDTO, pageable);
		
		Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageBoard ->
															BoardDTO.builder()
																	.boardNo(pageBoard.getBoardNo())
																	.cateNo(pageBoard.getCateNo())
																	.boardTitle(pageBoard.getBoardTitle())
																	.boardContent(pageBoard.getBoardContent())
																	.boardWriter(pageBoard.getBoardWriter())
																	.boardRegdate(pageBoard.getBoardRegdate())
																	.boardCnt(pageBoard.getBoardCnt())
																	.build()
															); */
		
		List<BoardDTO> getBoardList = new ArrayList<BoardDTO>();
		for(int i = 0; i < boardList.size(); i++) {
			BoardDTO returnBoard = BoardDTO.builder()
											.boardNo(boardList.get(i).getBoardNo())
											.cateNo(boardList.get(i).getCateNo())
											.boardTitle(boardList.get(i).getBoardTitle())
											.boardContent(boardList.get(i).getBoardContent())
											.boardWriter(boardList.get(i).getBoardWriter())
											.boardRegdate(boardList.get(i).getBoardRegdate())
											.boardCnt(boardList.get(i).getBoardCnt())
											.build();
			getBoardList.add(returnBoard);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getBoardList.html");
		mv.addObject("getBoardList", getBoardList);
		
		
/*		if(boardDTO.getSearchCondition() != null && !boardDTO.getSearchCondition().equals("")) {
			mv.addObject("searchCondition", boardDTO.getSearchCondition());
		}
		
		if(boardDTO.getSearchKeyword() != null && !boardDTO.getSearchKeyword().equals("")) {
			mv.addObject("searchKeyword", boardDTO.getSearchKeyword());
		} */
		
		return mv;
	}
	
	// 게시글 등록
	@GetMapping("/insertBoard")
	public ModelAndView insertBoardView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("/board/insertBoard.html");
		
		return mv;
	}
	
	@PostMapping("/board")
	public void insertBoard(BoardDTO boardDTO, MultipartFile[] uploadFiles,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		// DB에 입력될 파일 정보 리스트
		List<BoardFileDTO> uploadFileList = new ArrayList<BoardFileDTO>();
		
		// 업로드할 파일이 있는 경우
		if(uploadFiles.length > 0) {
			String attachPath = request.getSession().getServletContext().getRealPath("/") + "/upload/";
			
			File directory = new File(attachPath);
			
			// 업로드한 파일 경로가 없는 경우
			if(!directory.exists()) {
				directory.mkdir();
			}
			
			// multipartFile 형식의 데이터를 DB 테이블에 맞는 구조로 변경
			for(int i = 0; i < uploadFiles.length; i++) {
				MultipartFile file = uploadFiles[i];
				
				if(!file.getOriginalFilename().equals("") &&
						file.getOriginalFilename() != null) {
					BoardFileDTO boardFile = new BoardFileDTO();
					
					boardFile = FileUtils.parseFileInfo(file, attachPath);
					
					uploadFileList.add(boardFile);
				}
			}
		}
		
		
		boardService.insertBoard(boardDTO, uploadFileList);
		
		response.sendRedirect("/board/boardList");
	}
	
	// 조회수 증가
	@GetMapping("/updateBoardCnt/{boardNo}")
	public void updateBoardCnt(@PathVariable int boardNo, HttpServletResponse response) throws IOException {
		boardService.updateBoardCnt(boardNo);
		
		response.sendRedirect("/board/board/" + boardNo);
	}
	
	// 게시글 상세
	@GetMapping("/board/{boardNo}")
	public ModelAndView getBoard(@PathVariable int boardNo) {
		BoardDTO board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.cateNo(board.getCateNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.boardRegdate(
											board.getBoardRegdate() == null ?
											null :
											board.getBoardRegdate().toString())
									.boardCnt(board.getBoardCnt())
									.build();
		
		List<BoardFileDTO> boardFileList = boardService.getBoardFileList(boardDTO.getBoardNo());
		
		List<BoardFileDTO> boardFileDTOList = new ArrayList<BoardFileDTO>();
		
		for(BoardFileDTO boardFile : boardFileList) {
			BoardFileDTO boardFileDTO = BoardFileDTO.builder()
													.boardNo(boardNo)
													.fileNo(boardNo)
													.fileNo(boardFile.getFileNo())
													.fileName(boardFile.getFileName())
													.fileOriName(boardFile.getFileOriName())
													.filePath(boardFile.getFilePath())
													.fileCate(boardFile.getFileCate())
													.build();
			
			boardFileDTOList.add(boardFileDTO);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getBoard.html");
		mv.addObject("getBoard", boardDTO);
		mv.addObject("boardFileList", boardFileDTOList);
		
		return mv;
	}
	
	// 게시글 삭제
	@DeleteMapping("/board")
	public void deleteBoard(@RequestParam("boardNo") int boardNo) {
		boardService.deleteBoard(boardNo);
	}
	
	// 게시글 수정
	@GetMapping("/updateBoard/{boardNo}")
	public ModelAndView updateBoardView(@PathVariable("boardNo") int boardNo) {
		BoardDTO board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.cateNo(board.getCateNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.build();
		
		List<BoardFileDTO> boardFileList = boardService.getBoardFileList(boardDTO.getBoardNo());
		
		List<BoardFileDTO> boardFileDTOList = new ArrayList<BoardFileDTO>();
		
		for(BoardFileDTO boardFile : boardFileList) {
			BoardFileDTO boardFileDTO = BoardFileDTO.builder()
													.boardNo(boardNo)
													.fileNo(boardNo)
													.fileNo(boardFile.getFileNo())
													.fileName(boardFile.getFileName())
													.fileOriName(boardFile.getFileOriName())
													.filePath(boardFile.getFilePath())
													.fileCate(boardFile.getFileCate())
													.build();
			
			boardFileDTOList.add(boardFileDTO);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/updateBoard.html");
		mv.addObject("getBoard", boardDTO);
		mv.addObject("boardFileList", boardFileDTOList);
		
		return mv;
	}
	
	@PostMapping("/updateBoard")
	public void updateBoard(BoardDTO boardDTO,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		BoardDTO board = BoardDTO.builder()
								 .boardNo(boardDTO.getBoardNo())
								 .cateNo(boardDTO.getCateNo())
								 .boardTitle(boardDTO.getBoardTitle())
								 .boardContent(boardDTO.getBoardContent())
								 .boardWriter(boardDTO.getBoardWriter())
								 .build();
		
		boardService.updateBoard(boardDTO);
		
		response.sendRedirect("board/" + board.getBoardNo());
	}
}
