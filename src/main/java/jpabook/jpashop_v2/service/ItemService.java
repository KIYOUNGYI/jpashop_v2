package jpabook.jpashop_v2.service;

import jpabook.jpashop_v2.domain.Item;
import jpabook.jpashop_v2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService
{
    private final ItemRepository itemRepository;

    @Transactional//오버라이딩 한 것이라 보면 됨 (가까운게 더 우선권을 갖는다)
    public void saveItem(Item item)
    {
        itemRepository.save(item);
    }

    public List<Item> findItems()
    {
        return itemRepository.findAll();
    }

    public Item findOne(Long id)
    {
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
}
