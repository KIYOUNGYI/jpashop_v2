package jpabook.jpashop_v2.service;

import java.util.List;
import jpabook.jpashop_v2.domain.Book;
import jpabook.jpashop_v2.domain.Item;
import jpabook.jpashop_v2.dto.ItemUpdateRequestDto;
import jpabook.jpashop_v2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

  private final ItemRepository itemRepository;

  @Transactional//오버라이딩 한 것이라 보면 됨 (가까운게 더 우선권을 갖는다)
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findOne(Long id) {
    return itemRepository.findOne(id);
  }

  /**
   * 영속성 컨텍스트가 자동 변경
   */
  @Transactional
  public void updateItem(Long id, String name, int price) {
    Item item = itemRepository.findOne(id);
    item.setName(name);
    item.setPrice(price);
  }

  /**
   * (변경 감지) 영속성 컨텍스트가 자동 변경
   */
  @Transactional
  public void updateItem(Long id, Book param) {//param : 파라미터로 넘어온 준영속 상태의 엔티티
    Item item = itemRepository.findOne(id);// 같은엔티티를 조회한다.
    item.setName(param.getName());
    item.setPrice(param.getPrice());
    item.setStockQuantity(param.getStockQuantity());
  }

  /**
   * 영속성 컨텍스트가 자동 변경
   */
  @Transactional
  public void updateItem(Long id, ItemUpdateRequestDto dto) {
    Item item = itemRepository.findOne(id);
    item.setName(dto.getName());
    item.setPrice(dto.getPrice());
  }
}
