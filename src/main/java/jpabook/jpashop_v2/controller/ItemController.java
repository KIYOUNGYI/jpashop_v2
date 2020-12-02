package jpabook.jpashop_v2.controller;


import java.util.List;
import jpabook.jpashop_v2.domain.Book;
import jpabook.jpashop_v2.domain.Item;
import jpabook.jpashop_v2.dto.ItemUpdateRequestDto;
import jpabook.jpashop_v2.service.ItemService;
import jpabook.jpashop_v2.web.BookForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping(value = "/items/new")
  public String createForm(Model model) {
    model.addAttribute("form", new BookForm());
    return "items/createItemForm";
  }

  @PostMapping(value = "/items/new")
  public String create(BookForm form) {
    Book book = new Book();
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());
    itemService.saveItem(book);
    return "redirect:/items";
  }

  /**
   * 상품 목록
   */
  @GetMapping(value = "/items")
  public String list(Model model) {
    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  /**
   * 상품 수정 폼
   */
  @GetMapping(value = "/items/{itemId}/edit")
  public String updateItemForm(@PathVariable("itemId") Long itemId, Model
      model) {
    Book item = (Book) itemService.findOne(itemId);
    BookForm form = new BookForm();
    form.setId(item.getId());
    form.setName(item.getName());
    form.setPrice(item.getPrice());
    form.setStockQuantity(item.getStockQuantity());
    form.setAuthor(item.getAuthor());
    form.setIsbn(item.getIsbn());
    model.addAttribute("form", form);
    return "items/updateItemForm";
  }

  /**
   * 상품 수정
   */
  @PostMapping(value = "/items/{itemId}/edit")
  public String updateItem(@ModelAttribute("form") BookForm form) {
    Book book = new Book();
    book.setId(form.getId());
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());
    itemService.saveItem(book);
    return "redirect:/items";
  }

  /**
   * 쬐끔 더 나은 방법 (dto 로 파라미터 개수 줄이고) 수정할 녀석들만 명확하게 지정해서 서비스 계층에 전달
   */
  @PostMapping(value = "/items/{itemId}/edit/v2")
  public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
    ItemUpdateRequestDto dto = new ItemUpdateRequestDto();
    dto.setName(form.getName());
    dto.setPrice(form.getPrice());
    dto.setStockQuantity(form.getStockQuantity());
    itemService.updateItem(itemId, dto);
    return "redirect:/items";
  }

}