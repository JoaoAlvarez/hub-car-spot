import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITaxas } from '../taxas.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../taxas.test-samples';

import { TaxasService } from './taxas.service';

const requireRestSample: ITaxas = {
  ...sampleWithRequiredData,
};

describe('Taxas Service', () => {
  let service: TaxasService;
  let httpMock: HttpTestingController;
  let expectedResult: ITaxas | ITaxas[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TaxasService);
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

    it('should create a Taxas', () => {
      const taxas = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(taxas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Taxas', () => {
      const taxas = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(taxas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Taxas', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Taxas', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Taxas', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTaxasToCollectionIfMissing', () => {
      it('should add a Taxas to an empty array', () => {
        const taxas: ITaxas = sampleWithRequiredData;
        expectedResult = service.addTaxasToCollectionIfMissing([], taxas);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taxas);
      });

      it('should not add a Taxas to an array that contains it', () => {
        const taxas: ITaxas = sampleWithRequiredData;
        const taxasCollection: ITaxas[] = [
          {
            ...taxas,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTaxasToCollectionIfMissing(taxasCollection, taxas);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Taxas to an array that doesn't contain it", () => {
        const taxas: ITaxas = sampleWithRequiredData;
        const taxasCollection: ITaxas[] = [sampleWithPartialData];
        expectedResult = service.addTaxasToCollectionIfMissing(taxasCollection, taxas);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxas);
      });

      it('should add only unique Taxas to an array', () => {
        const taxasArray: ITaxas[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const taxasCollection: ITaxas[] = [sampleWithRequiredData];
        expectedResult = service.addTaxasToCollectionIfMissing(taxasCollection, ...taxasArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taxas: ITaxas = sampleWithRequiredData;
        const taxas2: ITaxas = sampleWithPartialData;
        expectedResult = service.addTaxasToCollectionIfMissing([], taxas, taxas2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxas);
        expect(expectedResult).toContain(taxas2);
      });

      it('should accept null and undefined values', () => {
        const taxas: ITaxas = sampleWithRequiredData;
        expectedResult = service.addTaxasToCollectionIfMissing([], null, taxas, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taxas);
      });

      it('should return initial array if no Taxas is added', () => {
        const taxasCollection: ITaxas[] = [sampleWithRequiredData];
        expectedResult = service.addTaxasToCollectionIfMissing(taxasCollection, undefined, null);
        expect(expectedResult).toEqual(taxasCollection);
      });
    });

    describe('compareTaxas', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTaxas(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareTaxas(entity1, entity2);
        const compareResult2 = service.compareTaxas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareTaxas(entity1, entity2);
        const compareResult2 = service.compareTaxas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareTaxas(entity1, entity2);
        const compareResult2 = service.compareTaxas(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
