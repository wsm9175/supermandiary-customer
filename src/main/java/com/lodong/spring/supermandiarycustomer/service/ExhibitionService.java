package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.domain.exhibition.*;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.CustomerAddress;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.dto.exhibition.*;
import com.lodong.spring.supermandiarycustomer.repository.*;
import com.lodong.spring.supermandiarycustomer.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final ExhibitionApartmentRepository exhibitionApartmentRepository;
    private final UserCustomerRepository userCustomerRepository;
    private final ExhibitionBoardRepository exhibitionBoardRepository;
    private final ExhibitionCommentRepository exhibitionCommentRepository;

    @Transactional(readOnly = true)
    public List<ExhibitionDTO> getExhibitionDTOList(String uuid) throws NullPointerException {
        //유저의 등록 주소를 가져온다
        List<CustomerAddress> customerAddressList = customerAddressRepository
                .findByUserCustomer_Id(uuid)
                .orElseThrow(() -> new NullPointerException("해당 회원은 등록된 주소가 존재하지 않습니다."));

        //시군구를 추출한다.
        List<Integer> siggAreas = new ArrayList<>();
        Optional.ofNullable(customerAddressList).orElseGet(Collections::emptyList).forEach(customerAddress -> {
            if (customerAddress.getApartment() != null) {
                int siggCode = customerAddress.getApartment().getSiggAreas().getCode();
                int mod = siggCode % 10;
                siggAreas.add(siggCode);
                if (mod != 0) {
                    siggAreas.add(siggCode - mod);
                }
                siggAreas.add(customerAddress.getApartment().getSiggAreas().getCode());
            } else if (customerAddress.getOtherHome() != null) {
                int siggCode = customerAddress.getOtherHome().getSiggAreas().getCode();
                int mod = siggCode % 10;
                siggAreas.add(siggCode);
                if (mod != 0) {
                    siggAreas.add(siggCode - mod);
                }
            }
        });

        //박람회 중 등록된 아파트가 siggArea에 속하는 아파트인지 판별에 데이터를 가져온다.
        LocalDateTime now = LocalDateTime.now();
        List<Exhibition> exhibitionList = exhibitionApartmentRepository.findByApartment_SiggAreas_CodeIn(siggAreas)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(ExhibitionApartment::getExhibition)
                .toList();

        List<ExhibitionDTO> exhibitionDTOS = new ArrayList<>();
        Optional.of(exhibitionList).orElseGet(Collections::emptyList)
                .forEach(exhibition -> {
                    if (exhibition.getStartDateTime().isBefore(now) && exhibition.getEndDateTime().isAfter(now)) {
                        ExhibitionDTO exhibitionDTO = new ExhibitionDTO(exhibition.getId(), exhibition.getName(), exhibition.getStartDateTime(), exhibition.getEndDateTime(), exhibition.isOfflineOn());
                        exhibitionDTOS.add(exhibitionDTO);
                    }
                });
        return exhibitionDTOS;
    }

    @Transactional(readOnly = true)
    public ExhibitionDetailDTO getExhibitionBoardList(String uuid, String exhibitionId) throws NullPointerException {
        UserCustomer userCustomer = userCustomerRepository.findById(uuid).orElseThrow(() -> new NullPointerException("해당 유저는 존재하지 않습니다."));
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).orElseThrow(() -> new NullPointerException("해당 박람회는 존재하지 않습니다."));
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        Optional.ofNullable(exhibition.getExhibitionCategoryList()).orElseGet(Collections::emptyList).forEach(exhibitionCategory -> {
            categoryDTOList.add(new CategoryDTO(exhibitionCategory.getId(), exhibitionCategory.getName()));
        });

        List<BoardDTO> boardDTOList = new ArrayList<>();
        Optional.ofNullable(exhibition.getExhibitionBoardList()).orElseGet(Collections::emptyList).forEach(exhibitionBoard -> {
            List<String> categoryIdList = new ArrayList<>();
            Optional.ofNullable(exhibitionBoard.getExhibitionCategoryIndexList()).orElseGet(Collections::emptyList).forEach(exhibitionCategoryIndex -> {
                categoryIdList.add(exhibitionCategoryIndex.getCategory().getId());
            });
            boardDTOList.add(new BoardDTO(exhibitionBoard.getId(), categoryIdList, exhibitionBoard.getVideoLink(), exhibitionBoard.getTag()));
        });
        return new ExhibitionDetailDTO(exhibition.getName(), userCustomer.getName(), categoryDTOList, boardDTOList);
    }

    @Transactional(readOnly = true)
    public BoardDetailDTO getBoardDetail(String uuid, String boardId) throws NullPointerException {
        UserCustomer userCustomer = userCustomerRepository.findById(uuid).orElseThrow(() -> new NullPointerException("해당 유저는 존재하지 않습니다."));
        ExhibitionBoard exhibitionBoard = exhibitionBoardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물은 존재하지 않습니다."));
        List<ExhibitionCommentDTO> exhibitionCommentDTOList = new ArrayList<>();
        exhibitionCommentRepository.findByExhibitionBoard_IdAndSequence(boardId, 0).orElseGet(Collections::emptyList).forEach(exhibitionComment -> {
            ExhibitionCommentDTO  exhibitionCommentDTO = null;
            if (exhibitionComment.getConstructor() != null) {
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionComment.getId(), exhibitionComment.getCommentGroupId(), exhibitionComment.getSequence(), exhibitionComment.getComment(), exhibitionComment.getConstructor().getName(), null, false, exhibitionComment.getCreateAt(), exhibitionComment.isHasCommentGroup());
            }else if(exhibitionComment.getUserCustomer() != null){
                boolean isMine = false;
                if(exhibitionComment.getUserCustomer().getId().equals(uuid)){
                    isMine = true;
                }
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionComment.getId(), exhibitionComment.getCommentGroupId(), exhibitionComment.getSequence(), exhibitionComment.getComment(), null, exhibitionComment.getUserCustomer().getName(), isMine, exhibitionComment.getCreateAt(), exhibitionComment.isHasCommentGroup());
            }
            exhibitionCommentDTOList.add(exhibitionCommentDTO);
        });
        return new BoardDetailDTO(exhibitionBoard.getConstructorName(), exhibitionBoard.getVideoLink(), exhibitionBoard.getTag(), Optional.ofNullable(exhibitionBoard.getConstructor().getConstructorPriceTableFiles()).orElseGet(Collections::emptyList).stream().map(constructorPriceTableFile -> constructorPriceTableFile.getFileList().getName()).toList(), exhibitionCommentDTOList);
    }

    @Transactional(readOnly = true)
    public List<ExhibitionCommentDTO> getExhibitionCommentGroup(String uuid, String commentGroupId) throws NullPointerException{
        UserCustomer userCustomer = userCustomerRepository.findById(uuid).orElseThrow(() -> new NullPointerException("해당 유저는 존재하지 않습니다."));
        List<ExhibitionComment> exhibitionCommentList = exhibitionCommentRepository
                .findByCommentGroupIdAndSequenceNot(commentGroupId, 0)
                .orElseThrow(()-> new NullPointerException("해당 댓글에는 대댓글이 존재하지 않습니다."));
        List<ExhibitionCommentDTO> exhibitionCommentDTOList = new ArrayList<>();
        Optional.ofNullable(exhibitionCommentList).orElseGet(Collections::emptyList).forEach(exhibitionComment -> {
            ExhibitionCommentDTO  exhibitionCommentDTO = null;
            if (exhibitionComment.getConstructor() != null) {
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionComment.getId(), exhibitionComment.getCommentGroupId(), exhibitionComment.getSequence(), exhibitionComment.getComment(), exhibitionComment.getConstructor().getName(), null, false, exhibitionComment.getCreateAt(), exhibitionComment.isHasCommentGroup());
            }else if(exhibitionComment.getUserCustomer() != null){
                boolean isMine = false;
                if(exhibitionComment.getUserCustomer().getId().equals(uuid)){
                    isMine = true;
                }
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionComment.getId(), exhibitionComment.getCommentGroupId(), exhibitionComment.getSequence(), exhibitionComment.getComment(), null, exhibitionComment.getUserCustomer().getName(), isMine, exhibitionComment.getCreateAt(), exhibitionComment.isHasCommentGroup());
            }
            exhibitionCommentDTOList.add(exhibitionCommentDTO);
        });

        return exhibitionCommentDTOList;
    }

    @Transactional
    public List<ExhibitionCommentDTO> addComment(String uuid, String boardId, String comment) {
        ExhibitionBoard exhibitionBoard = exhibitionBoardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물은 존재하지 않습니다."));
        UserCustomer userCustomer = UserCustomer.builder().id(uuid).build();
        ExhibitionComment exhibitionComment = ExhibitionComment.builder().id(UUID.randomUUID().toString()).exhibitionBoard(exhibitionBoard).commentGroupId(UUID.randomUUID().toString()).sequence(0).comment(comment).userCustomer(userCustomer).hasCommentGroup(false).createAt(DateUtil.getNowDateTime()).build();
        exhibitionCommentRepository.save(exhibitionComment);
        List<ExhibitionCommentDTO> exhibitionCommentDTOList = new ArrayList<>();
        exhibitionCommentRepository.findByExhibitionBoard_IdAndSequence(boardId, 0).orElseGet(Collections::emptyList).forEach(exhibitionComment1 -> {
            ExhibitionCommentDTO  exhibitionCommentDTO = null;
            if (exhibitionComment1.getConstructor() != null) {
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionComment1.getId(), exhibitionComment1.getCommentGroupId(), exhibitionComment1.getSequence(), exhibitionComment1.getComment(), exhibitionComment1.getConstructor().getName(), null, false, exhibitionComment1.getCreateAt(), exhibitionComment1.isHasCommentGroup());
            }else if(exhibitionComment1.getUserCustomer() != null){
                boolean isMine = false;
                if(exhibitionComment1.getUserCustomer().getId().equals(uuid)){
                    isMine = true;
                }
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionComment1.getId(), exhibitionComment1.getCommentGroupId(), exhibitionComment1.getSequence(), exhibitionComment1.getComment(), null,  exhibitionComment1.getUserCustomer().getName(), isMine, exhibitionComment1.getCreateAt(), exhibitionComment1.isHasCommentGroup());
            }
            exhibitionCommentDTOList.add(exhibitionCommentDTO);
        });

        return exhibitionCommentDTOList;
    }

    @Transactional
    public List<ExhibitionCommentDTO> addReply(String uuid, String boardId, String comment, String commentGroupId, int sequence) {
        ExhibitionBoard exhibitionBoard = ExhibitionBoard.builder().id(boardId).build();
        UserCustomer userCustomer = UserCustomer.builder().id(uuid).build();
        ExhibitionComment exhibitionComment = ExhibitionComment.builder().id(UUID.randomUUID().toString()).exhibitionBoard(exhibitionBoard).commentGroupId(commentGroupId).sequence(sequence).comment(comment).userCustomer(userCustomer).hasCommentGroup(false).createAt(DateUtil.getNowDateTime()).build();
        exhibitionCommentRepository.save(exhibitionComment);
        exhibitionCommentRepository.updateHasCommentGroup(true, commentGroupId);

        List<ExhibitionComment> exhibitionCommentList = exhibitionCommentRepository
                .findByCommentGroupIdAndSequenceNot(commentGroupId, 0)
                .orElseThrow(()-> new NullPointerException("해당 댓글에는 대댓글이 존재하지 않습니다."));
        List<ExhibitionCommentDTO> exhibitionCommentDTOList = new ArrayList<>();
        Optional.ofNullable(exhibitionCommentList).orElseGet(Collections::emptyList).forEach(exhibitionComment1 -> {
            ExhibitionCommentDTO  exhibitionCommentDTO = null;
            if (exhibitionComment1.getConstructor() != null) {
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionComment1.getId(), exhibitionComment1.getCommentGroupId(), exhibitionComment1.getSequence(), exhibitionComment1.getComment(), exhibitionComment1.getConstructor().getName(), null, false, exhibitionComment1.getCreateAt(), exhibitionComment1.isHasCommentGroup());
            }else if(exhibitionComment1.getUserCustomer() != null){
                boolean isMine = false;
                if(exhibitionComment1.getUserCustomer().getId().equals(uuid)){
                    isMine = true;
                }
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionComment1.getId(), exhibitionComment1.getCommentGroupId(), exhibitionComment1.getSequence(), exhibitionComment1.getComment(), null, exhibitionComment1.getUserCustomer().getName(), isMine, exhibitionComment1.getCreateAt(), exhibitionComment1.isHasCommentGroup());
            }
            exhibitionCommentDTOList.add(exhibitionCommentDTO);
        });
        return exhibitionCommentDTOList;
    }
    @Transactional
    public void deleteComment(String uuid, String commentId) throws NullPointerException{
        ExhibitionComment exhibitionComment = exhibitionCommentRepository.findByUserCustomer_IdAndId(uuid, commentId)
                .orElseThrow(()-> new NullPointerException("해당 댓글은 본인이 작성한 댓글이 아닙니다."));
        exhibitionCommentRepository.delete(exhibitionComment);
    }

    @Transactional
    public ExhibitionCommentDTO alterComment(String uuid, ExhibitionCommentAlterDTO exhibitionCommentAlterDTO){
        ExhibitionComment exhibitionComment = exhibitionCommentRepository.findByUserCustomer_IdAndId(uuid, exhibitionCommentAlterDTO.getCommentId())
                .orElseThrow(()-> new NullPointerException("해당 댓글은 본인이 작성한 댓글이 아닙니다."));
        //exhibitionCommentRepository.updateComment(exhibitionCommentAlterDTO.getContents(), uuid, exhibitionCommentAlterDTO.getCommentId());
        /*ExhibitionComment exhibitionCommentUpdate = exhibitionCommentRepository.findByUserCustomer_IdAndId(uuid, exhibitionCommentAlterDTO.getCommentId())
                .orElseThrow(()-> new NullPointerException("댓글 업데이트 실패"));*/
        exhibitionComment.setComment(exhibitionCommentAlterDTO.getContents());
        ExhibitionComment exhibitionCommentUpdate = exhibitionCommentRepository.save(exhibitionComment);
        return new ExhibitionCommentDTO(exhibitionCommentUpdate.getId(), exhibitionCommentUpdate.getCommentGroupId(), exhibitionCommentUpdate.getSequence(), exhibitionCommentUpdate.getComment(), null, exhibitionCommentUpdate.getUserCustomer().getName(), true, exhibitionCommentUpdate.getCreateAt(), exhibitionCommentUpdate.isHasCommentGroup());
    }
}
