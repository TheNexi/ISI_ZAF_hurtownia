import '@testing-library/jest-dom';

window.matchMedia = window.matchMedia || function () {
  return {
    matches: false,
    media: '',
    onchange: null,
    addListener: jest.fn(), 
    removeListener: jest.fn(), 
    addEventListener: jest.fn(), 
    removeEventListener: jest.fn(), 
    dispatchEvent: jest.fn(),
  };
};


class ResizeObserver {
  observe() {}
  unobserve() {}
  disconnect() {}
}

global.ResizeObserver = ResizeObserver as any;