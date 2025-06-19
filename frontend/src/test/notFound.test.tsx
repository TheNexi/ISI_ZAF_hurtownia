import React from 'react'
import { render, screen } from '@testing-library/react'
import NotFound from '../pages/NotFound'  
import { MemoryRouter } from 'react-router-dom'

describe('NotFound component', () => {
  test('renders 404 title and subtitle', () => {
    render(
      <MemoryRouter>
        <NotFound />
      </MemoryRouter>
    )

    expect(screen.getByText('404')).toBeInTheDocument()
    expect(screen.getByText(/Przepraszamy, ale strona, której szukasz, nie została znaleziona./i)).toBeInTheDocument()

    const linkElement = screen.getByRole('link', { name: /powrót do strony głównej/i })
    expect(linkElement).toBeInTheDocument()
    expect(linkElement).toHaveAttribute('href', '/')
  })
})
