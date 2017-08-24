(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('thought-of-day', {
            parent: 'entity',
            url: '/thought-of-day?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ThoughtOfDays'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/thought-of-day/thought-of-days.html',
                    controller: 'ThoughtOfDayController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('thought-of-day-detail', {
            parent: 'thought-of-day',
            url: '/thought-of-day/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ThoughtOfDay'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/thought-of-day/thought-of-day-detail.html',
                    controller: 'ThoughtOfDayDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ThoughtOfDay', function($stateParams, ThoughtOfDay) {
                    return ThoughtOfDay.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'thought-of-day',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('thought-of-day-detail.edit', {
            parent: 'thought-of-day-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thought-of-day/thought-of-day-dialog.html',
                    controller: 'ThoughtOfDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ThoughtOfDay', function(ThoughtOfDay) {
                            return ThoughtOfDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('thought-of-day.new', {
            parent: 'thought-of-day',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thought-of-day/thought-of-day-dialog.html',
                    controller: 'ThoughtOfDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                title: null,
                                url: null,
                                shortText: null,
                                longText: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('thought-of-day', null, { reload: 'thought-of-day' });
                }, function() {
                    $state.go('thought-of-day');
                });
            }]
        })
        .state('thought-of-day.edit', {
            parent: 'thought-of-day',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thought-of-day/thought-of-day-dialog.html',
                    controller: 'ThoughtOfDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ThoughtOfDay', function(ThoughtOfDay) {
                            return ThoughtOfDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('thought-of-day', null, { reload: 'thought-of-day' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('thought-of-day.delete', {
            parent: 'thought-of-day',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thought-of-day/thought-of-day-delete-dialog.html',
                    controller: 'ThoughtOfDayDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ThoughtOfDay', function(ThoughtOfDay) {
                            return ThoughtOfDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('thought-of-day', null, { reload: 'thought-of-day' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
