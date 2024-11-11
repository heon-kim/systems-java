package bookstore;

import java.time.LocalDate;
import java.util.*;

public class ConanBooks {
    private static List<Book> bookCatalog = new ArrayList<>();
    private static List<Book> cart = new ArrayList<>();
    private static Customer customer;

    public static void main(String[] args) {
        initializeBooks();
        getCustomerInfo();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printWelcomeMessage();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1->showCustomerInfo();
                case 2->showCart();
                case 3->addToCart(scanner);
                case 4->removeFromCart(scanner);
                case 5->clearCart();
                case 6->showReceipt(scanner);
                case 7->{
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default->System.out.println("올바른 메뉴 번호를 선택해주세요.");
            }
        }
    }

    private static void initializeBooks() {
        bookCatalog.add(new Book(1, "셜록홈즈", 20000, "코난도일", "그 누구도 뛰어넘지 못했던 추리소설의 고전", "추리소설", "2018/10/08"));
        bookCatalog.add(new Book(2, "도리안 그레이의 초상", 16000, "오스카 와일드", "예술을 위한 예술", "고전소설", "2022/01/22"));
        bookCatalog.add(new Book(3, "쥐덫", 27000, "애거서 크리스티", "폭설 속에 갇힌 몽스웰 여관", "추리소설", "2019/06/10"));
    }

    private static void getCustomerInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("당신의 이름을 입력하세요 : ");
        String name = scanner.nextLine();
        System.out.print("연락처를 입력하세요 : ");
        String contact = scanner.nextLine();
        customer = new Customer(name, contact);
    }

    private static void printWelcomeMessage() {
        System.out.println("*****************************************");
        System.out.println("오늘의 선택, 코난문고");
        System.out.println("영원한 스테디셀러, 명탐정 코난 시리즈를 만나보세요~");
        System.out.println("*****************************************");
        System.out.println("1. 고객 정보 확인하기 2. 장바구니 상품 목록 보기");
        System.out.println("3. 바구니에 항목 추가하기 4. 장바구니의 항목 삭제하기");
        System.out.println("5. 장바구니 비우기 6. 영수증 표시하기 7. 종료");
        System.out.println("*****************************************");
        System.out.print("메뉴번호를 선택해주세요 ");
    }

    private static void showCustomerInfo() {
        System.out.println("현재 고객 정보:");
        customer.showInfo();
    }

    private static void showCart() {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있습니다.");
        } else {
            System.out.println("장바구니 상품 목록:");
            System.out.println("도서ID | 수량 | 합계");

            Map<Integer, Integer> quantityMap = new HashMap<>();
            Map<Integer, Integer> totalPriceMap = new HashMap<>();

            for (Book book : cart) {
                int bookId = book.getId();
                quantityMap.put(bookId, quantityMap.getOrDefault(bookId, 0) + 1);
                totalPriceMap.put(bookId, totalPriceMap.getOrDefault(bookId, 0) + book.getPrice());
            }

            for (Book book : bookCatalog) {
                int bookId = book.getId();
                if (quantityMap.containsKey(bookId)) {
                    System.out.println(bookId + " | " + quantityMap.get(bookId) + " | " + totalPriceMap.get(bookId) + "원");
                }
            }
        }
    }

    private static void addToCart(Scanner scanner) {
        for (Book book : bookCatalog) {
            System.out.println(book);
        }

        System.out.println("장바구니에 추가할 도서의 ID를 입력하세요:");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("장바구니에 추가하겠습니까? Y | N ");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("Y")) {
            for (Book book : bookCatalog) {
                if (book.getId() == bookId) {
                    cart.add(book);
                    System.out.println("장바구니에 추가되었습니다.");
                    return;
                }
            }
            System.out.println("유효한 도서 ID가 아닙니다.");
        } else {
            System.out.println("추가가 취소되었습니다.");
        }
    }

    private static void removeFromCart(Scanner scanner) {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있습니다.");
            return;
        }

        showCart();
        System.out.println("장바구니에서 삭제할 도서의 ID를 입력하세요:");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        for (Book book : cart) {
            if (book.getId() == bookId) {
                cart.remove(book);
                System.out.println("장바구니에서 " + bookId + "가 삭제되었습니다.");
                return;
            }
        }
        System.out.println("장바구니에 해당 도서가 없습니다.");
    }

    private static void clearCart() {
        cart.clear();
        System.out.println("장바구니를 비웠습니다.");
    }


    private static void showReceipt(Scanner scanner) {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있습니다.");
            return;
        }

        System.out.print("배송받을 분은 고객 정보와 같습니까? ");
        Customer receiver;
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("Y")) {
            receiver = customer;
        } else {
            System.out.print("배송받을 분의 이름을 입력해주세요: ");
            String receiverName = scanner.nextLine();
            System.out.print("배송받을 분의 연락처를 입력해주세요: ");
            String receiverContact = scanner.nextLine();
            receiver = new Customer(receiverName, receiverContact);
        }

        System.out.print("배송지를 입력해주세요: ");
        String receiveAddress = scanner.nextLine();

        System.out.println("\n------------ 배송 받을 고객 정보 ------------");
        System.out.println("고객명: " + receiver.getName() + "연락처: " + receiver.getContact());
        System.out.println("배송지: " + receiveAddress + "발송일: " + LocalDate.now());

        System.out.println("\n장바구니 상품 목록:");
        System.out.println("-------------------------------");
        showCart();
        int total = 0;
        for (Book book : cart) {
            total += book.getPrice();
        }
        System.out.println("총 금액: " + total + "원");
        System.out.println("-------------------------------");
    }
}
