import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFinanceira } from '../financeira.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../financeira.test-samples';

import { FinanceiraService } from './financeira.service';

const requireRestSample: IFinanceira = {
  ...sampleWithRequiredData,
};

describe('Financeira Service', () => {
  let service: FinanceiraService;
  let httpMock: HttpTestingController;
  let expectedResult: IFinanceira | IFinanceira[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FinanceiraService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Financeira', () => {
      const financeira = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(financeira).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Financeira', () => {
      const financeira = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(financeira).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Financeira', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Financeira', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Financeira', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFinanceiraToCollectionIfMissing', () => {
      it('should add a Financeira to an empty array', () => {
        const financeira: IFinanceira = sampleWithRequiredData;
        expectedResult = service.addFinanceiraToCollectionIfMissing([], financeira);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(financeira);
      });

      it('should not add a Financeira to an array that contains it', () => {
        const financeira: IFinanceira = sampleWithRequiredData;
        const financeiraCollection: IFinanceira[] = [
          {
            ...financeira,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFinanceiraToCollectionIfMissing(financeiraCollection, financeira);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Financeira to an array that doesn't contain it", () => {
        const financeira: IFinanceira = sampleWithRequiredData;
        const financeiraCollection: IFinanceira[] = [sampleWithPartialData];
        expectedResult = service.addFinanceiraToCollectionIfMissing(financeiraCollection, financeira);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(financeira);
      });

      it('should add only unique Financeira to an array', () => {
        const financeiraArray: IFinanceira[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const financeiraCollection: IFinanceira[] = [sampleWithRequiredData];
        expectedResult = service.addFinanceiraToCollectionIfMissing(financeiraCollection, ...financeiraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const financeira: IFinanceira = sampleWithRequiredData;
        const financeira2: IFinanceira = sampleWithPartialData;
        expectedResult = service.addFinanceiraToCollectionIfMissing([], financeira, financeira2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(financeira);
        expect(expectedResult).toContain(financeira2);
      });

      it('should accept null and undefined values', () => {
        const financeira: IFinanceira = sampleWithRequiredData;
        expectedResult = service.addFinanceiraToCollectionIfMissing([], null, financeira, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(financeira);
      });

      it('should return initial array if no Financeira is added', () => {
        const financeiraCollection: IFinanceira[] = [sampleWithRequiredData];
        expectedResult = service.addFinanceiraToCollectionIfMissing(financeiraCollection, undefined, null);
        expect(expectedResult).toEqual(financeiraCollection);
      });
    });

    describe('compareFinanceira', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFinanceira(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareFinanceira(entity1, entity2);
        const compareResult2 = service.compareFinanceira(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareFinanceira(entity1, entity2);
        const compareResult2 = service.compareFinanceira(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareFinanceira(entity1, entity2);
        const compareResult2 = service.compareFinanceira(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
