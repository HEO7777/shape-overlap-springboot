class ShapeVisualizer {
    constructor() {
        this.canvas = document.getElementById('shapeCanvas');
        this.ctx = this.canvas.getContext('2d');
        this.jsonDataElement = document.getElementById('jsonData');
        this.statsElement = document.getElementById('stats');
        this.currentShapes = null;
        this.currentResponseData = null;

        // 이벤트 리스너 설정
        document.getElementById('generateBtn').addEventListener('click', () => this.generateShapes());
        document.getElementById('showJsonBtn').addEventListener('click', () => this.showJsonModal());
        window.addEventListener('resize', () => this.updateCanvasSize());

        // 모달 외부 클릭 시 닫기
        document.getElementById('jsonModal').addEventListener('click', (e) => {
            if (e.target.id === 'jsonModal') {
                this.closeJsonModal();
            }
        });

        // ESC 키로 모달 닫기
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                this.closeJsonModal();
            }
        });

        // 초기 캔버스 크기 설정
        this.updateCanvasSize();
    }

    updateCanvasSize() {
        const width = Math.max(800, window.innerWidth - 100);
        const height = Math.max(500, window.innerHeight - 400);

        this.canvas.width = width;
        this.canvas.height = height;
        this.canvas.style.width = width + 'px';
        this.canvas.style.height = height + 'px';
    }

    async generateShapes() {
        // 추후 작성
    }

    showJsonModal() {
        if (!this.currentResponseData) {
            alert('먼저 도형을 생성해주세요.');
            return;
        }

        document.getElementById('jsonModal').style.display = 'block';
    }

    closeJsonModal() {
        document.getElementById('jsonModal').style.display = 'none';
    }
}

// 전역 함수로 모달 닫기 함수 정의
function closeJsonModal() {
    document.getElementById('jsonModal').style.display = 'none';
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    const visualizer = new ShapeVisualizer();
    // 첫 번째 도형 자동 생성
    visualizer.generateShapes();
});