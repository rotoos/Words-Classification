# Words-Classification
Words Classification

# 주요 고려사항
1. 전역적으로 자주 사용되는 변수들은 번거롭게 매번 파라미터로 전달하지 않고, 가독성과 공통 관리를 위해 별도 클래스로 분리
2. 동일한 단어는 항상 같은 파티션에 기록하기 위해 HashCode를 사용.
3. 데이터의 유효성을 검증하는 기능은 변화할 수 있다고 판단하여, 인터페이스로 설계 
4. CountDownLatch를 활용하여 Graceful Shutdown 수행하고자 함.

# 클래스 설계

com.kakao.work
     |- context
           |- DataManager.java : 전역적으로 자주 사용되는 데이터들을 관리.
           |- LatchManager.java : CountDownLatch를 관리.
           
     |- file 
           |- FileAppendManager.java : File에 데이터를 기록하는 일을 담당.
           |- FilePoolManager.java : Prefix별로 생성되는 BufferedWriter들을 Map으로 관리.
           
     |- processor
           |- ClassificationConsumer.java : Consumer 역할을 수행 - Queue(파티션)마다 하나의 Consumer를 통해 데이터를 읽어들여 File에 기록.
           |- ClassificationManager.java : Producer와 Consumer의 생성 & 병렬 처리를 위한 Queue 관리 및 데이터 기록.
           |- ClassificationProducer.java : Producer 역할을 수행 - File로 부터 데이터를 읽고, Queue(파티션)에 데이터를 적절히 배분.
           
     |- verifier
           |- RegexBasedVerifier.java : 정규표현식 기반으로 String 데이터의 유효성을 검증.
           |- StringVerifier.java : String 데이터의 유효성을 검증하는 기능의 인터페이스.
           
     |- Main.java : main 메소드가 포함되어 있는 클래스.
